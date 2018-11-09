	package rt;

	import java.io.BufferedReader;
	import java.io.FileOutputStream;
	import java.io.File;
	import java.io.FileReader;
	import java.io.IOException;
	import java.io.FileInputStream;
	import java.io.InputStream;
import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.io.InputStreamReader;
	import java.util.ArrayList;
	import java.util.List;
	import jxl.Sheet;
	import jxl.Workbook;
	import jxl.read.biff.BiffException;
	import jxl.write.Label;
	import jxl.write.WritableSheet;
	import jxl.write.WritableWorkbook;
	import jxl.write.WriteException;
	import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import jxl.write.biff.RowsExceededException;
	/**
	 *
	 * 
	 * @author Kurt
	 * @date 2010-11-4
	 */
	public final class XlsProcessor {
	 
	 private static final XlsProcessor processor = new XlsProcessor();
	 
	 private XlsProcessor(){
	 }
	 
	 public static XlsProcessor getInstance(){
	  return processor;
	 }
	 
	 
	 public String saveFileToLocal(InputStream is,String path)throws IOException, RowsExceededException, WriteException{
		 String newfileFullname=path+"\\"+"tmp_product_label"+getUniqeDateTime()+".xls";
		 
		 File xls = new File(newfileFullname);
		 //System.err.println("123");
		 if(!xls.createNewFile())
			 return null;
		 
		 FileOutputStream fos = null;
		 
		 try{
			   fos = new FileOutputStream(xls);
			   int bytesRead;
			   byte[] buf = new byte[4 * 1024]; //4K buffer
			   while((bytesRead = is.read(buf)) != -1){
			    fos.write(buf, 0, bytesRead);
			   }
			   fos.flush();
			   fos.close();
			   is.close();
			   return newfileFullname;
			  }catch(IOException e)
			  {
			   System.out.println(e);
			   
			  }
		 
		 
			  return null;
	 }
	 
	 
	 
	 
	 public POIFSFileSystem convert(InputStream is) throws IOException, RowsExceededException, WriteException{
		  String Filename="tmp"+getUniqeDateTime()+".xls";
		  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		  //OutputStream os = new FileOutputStream(Filename);
		 // PrintWriter writer =new PrintWriter(new OutputStreamWriter(os));
        // System.err.println("123");
		  String xlsFileName = getFileName(Filename);
		  File xls = new File(xlsFileName);
		  System.err.println("xlsFileName="+xlsFileName);
		  WritableWorkbook workbook = Workbook.createWorkbook(xls);
		  WritableSheet sheet = workbook.createSheet("CSV", 0);
		  int rowNumber = 0;
		  String row = bufferedReader.readLine();
		  
		  while(row != null){
		   String[] cells = row.split(",");
		   List<String> cellList = format(cells);
		   for(int i=0,size=cellList.size();i<size;i++){
		    sheet.addCell(new Label(i,rowNumber,cellList.get(i)));
		   }
		   rowNumber++;
		   row = bufferedReader.readLine();
		  }
		  workbook.write();
		  workbook.close();
		  POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(Filename));
		  System.err.println("end convert");
		  return fs;
		 }
	
	 
	 public static String getUniqeDateTime()
	 {
		 return  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	 }
	 
	 public File convert(File cvsFile) throws IOException, RowsExceededException, WriteException{
	  FileReader fileReader = new FileReader(cvsFile);
	  BufferedReader bufferedReader = new BufferedReader(fileReader);
	  String xlsFileName = getFileName(cvsFile.getName());
	  File xls = new File(xlsFileName);
	  System.err.println("xlsFileName="+xlsFileName);
	  WritableWorkbook workbook = Workbook.createWorkbook(xls);
	  WritableSheet sheet = workbook.createSheet("CSV", 0);
	  int rowNumber = 0;
	  String row = bufferedReader.readLine();
	  while(row != null){
	   String[] cells = row.split(",");
	   List<String> cellList = format(cells);
	   for(int i=0,size=cellList.size();i<size;i++){
	    sheet.addCell(new Label(i,rowNumber,cellList.get(i)));
	   }
	   rowNumber++;
	   row = bufferedReader.readLine();
	  }
	  workbook.write();
	  workbook.close();
	  return xls;
	 }
	 
	 public boolean compare(File a, File b) throws BiffException, IOException{
	  Workbook aa = Workbook.getWorkbook(a);
	  Workbook bb = Workbook.getWorkbook(b);
	  Sheet aaSheet = aa.getSheet(0);
	  Sheet bbSheet = bb.getSheet(0);
	  int aaSheetRows = aaSheet.getRows();
	  int bbSheetRows = bbSheet.getRows();
	  int aaSheetCols = aaSheet.getColumns();
	  int bbSheetCols = bbSheet.getColumns();
	  boolean result = true;
	  if(aaSheetRows == bbSheetRows && aaSheetCols == bbSheetCols){
	   for(int i=0;i<aaSheetRows;i++){
	    for(int j=0;j<aaSheetCols;j++){
	     String aaCell = aaSheet.getCell(j, i).getContents();
	     String bbCell = bbSheet.getCell(j, i).getContents();
	     if(!aaCell.equals(bbCell)){
	      System.out.println("Row:"+(i+1)+",Column:"+(j+1)+",one is "+aaCell+",the other is "+bbCell);
	      result = false;
	      break;
	     }
	    }
	    if(result){
	     continue;
	    }else{
	     break;
	    }
	   }
	  }
	  System.out.println("Compare result : "+result);
	  return result;
	 }
	 
	 private String getFileName(String csvFileName){
	  return csvFileName.substring(0, csvFileName.length()-4)+".xls";
	 }
	 
	 private List<String> format(String[] cells){
	  List<String> cellList = new ArrayList<String>();
	  StringBuffer buffer = new StringBuffer();
	  for(String cell : cells){
	   boolean isHead = cell.startsWith("\"");
	   boolean isTail = cell.endsWith("\"");
	   //System.err.println("===cell="+cell);
	   if(isHead){
	    if(isTail){
	    	//System.err.println("cell.string= "+cell.substring(0));
	     cellList.add(cell.substring(1, cell.length()-1));
	    }else{
	     buffer.append(cell.substring(1)).append(',');
	    }
	   }else{
	    if(isTail){
	     buffer.append(cell.substring(0, cell.length()-1));
	     cellList.add(buffer.toString());
	     buffer.delete(0, buffer.length());
	    }else if(buffer.length()>0){
	     buffer.append(cell).append(',');
	    }else{
	     cellList.add(cell);
	    }
	   }
	  }
	  return cellList;
	 }
	
	
	/*
	public static void main(String[] args) throws RowsExceededException, WriteException {
		  try {
		   XlsProcessor processor = XlsProcessor.getInstance();
		   String csv = "D:/Hermes/40098.csv";
		   File file = processor.convert(new File(csv));
		   //File referenceFile = new File("D:/Hermes/40099.xls");
		  // boolean same = processor.compare(file, referenceFile);
		  // System.out.println(same);
		  } 
		 // catch (BiffException e) {
		 //  e.printStackTrace();
		 // }
		  catch (IOException e) {
		   e.printStackTrace();
		  }
		 }*/
	 }