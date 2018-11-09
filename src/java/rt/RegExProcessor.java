package rt;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;

/**
*
* 
* @author Kurt
* @date 2012-3-13
*/

public class RegExProcessor {

	
    
	//public String patternString="[0-9]{6}[0-9A-Z[\\s]]{4,7}[\\s][0-9]{13}[\\s][0-9]+";
	//EAN 为13位数字或 1位字母+11位数字
	public String patternString="[0-9]{6}[0-9A-Z[\\s]]{4,7}[\\s][0-9A-Z][0-9]{11,12}[\\s][0-9]+";
	public String matchString="";
	
	
	 
	private static final RegExProcessor processor = new RegExProcessor();
	 
	 public RegExProcessor(){
	 }
	 
	 public static RegExProcessor getInstance(){
	  return processor;
	 }
	 
	 public void resetPatternString(){
		//EAN 为13位数字或 1位字母+11位数字
		// patternString="[0-9]{6}[0-9A-Z[\\s]]{4,7}[\\s][0-9]{13}[\\s][0-9]+";
		 patternString="[0-9]{6}[0-9A-Z[\\s]]{4,7}[\\s][0-9A-Z][0-9]{11,12}[\\s][0-9]+";
	 }
	 
	 
	
	public String getPatternString() {
		return patternString;
	}


	public void setPatternString(String patternString) {
		this.patternString = patternString;
	}


	public String getMatchString() {
		return matchString;
	}


	public void setMatchString(String matchString) {
		this.matchString = matchString;
	}


	public boolean isPatternMatch(){
		 try {
             Pattern pattern = Pattern.compile(".*"+patternString+".*");
             Matcher match = pattern.matcher(matchString);       
             
             return match.matches();
           } catch (Exception e) {
             return false;
           }
	}
	
	public String getPureData(){
		 try {
			 String ss=matchString;
             Pattern pattern = Pattern.compile(patternString+".*");
			 Matcher match = pattern.matcher(ss); 
			 while(ss!=null&&ss.length()>1&&!match.matches())
			 {
				 ss=ss.substring(1);
				 match = pattern.matcher(ss); 
				 
			 }
             
			  pattern = Pattern.compile(patternString);
			  match = pattern.matcher(ss); 
			 while(ss!=null&&ss.length()>1&&!match.matches())
			 {
				 ss=ss.substring(0, ss.length()-1);
				 match = pattern.matcher(ss); 
				 //System.err.println("llllllllllllllllllllllllls="+ss);
			 }
			 
			// System.err.println("ssssssssssssssssssssssss="+ss);
             return ss;
           } catch (Exception e) {
             return "";
           }
	}
	
	
	public static String getUniqeDateTime()
	 {
		 return  new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	 }
	//1111111111111111111111111111111111
	



//将pdf文件输出为txt
    public static void PDFtoTXT(File pdf) {
    	
    	System.err.println("PDFtoTXT start");
        PDDocument pd=null;
        BufferedWriter wr=null;
       
        try{
        try {
        	 File input = pdf; 
             // The PDF file from where
             // you would like to
             // extract
             File output = new File(pdf.getName().split("\\.")[0] + ".txt");
            // The text file where
            // you are going to
            // store the
            // extracted data
            pd = PDDocument.load(input);
           // pd.save("CopyOf" + pdf.getName().split("\\.")[0] + ".pdf"); // Creates a copy called
            // "CopyOfInvoice.pdf"
            
           
            PDFTextStripper stripper = new PDFTextStripper();
            String encoding ="UTF-8";
            wr = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(output),encoding));
            stripper.setSortByPosition(true);           
            stripper.writeText(pd, wr);
            if (pd != null) {
                pd.close();
            }
            // I use close() to flush the stream.
            //wr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(pd!=null)
        		pd.close();
        	if(wr!=null)
        	wr.close();
    	 }
        
        }
        catch (Exception e) {
            e.printStackTrace();
        }
   

    }
	
    public static void readFileByLines(String fileName)
    {
    	
    	
    }
    
    public String savePackingWeightFileToLocalPDF(InputStream is)throws IOException, RowsExceededException, WriteException{
		 String newfileFullname="tmp_packing_sheetCartonWeightPDF"+getUniqeDateTime()+".PDF";
		 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		 File pdf = new File(newfileFullname);
		 //System.err.println("123");
		 if(!pdf.createNewFile())
			 return null;
		 FileOutputStream fos=null;
		 try{
			 
			 fos = new FileOutputStream(newfileFullname);
			 
		 }catch(Exception e)
			{
				System.err.println("savePackingWeightFileToLocalPDF error"+e.getMessage());
			}
		 
		 int ch =0;
		 try{
			 
			 while((ch=is.read())!=-1){
				 fos.write(ch);
			 }
			 
		 }catch(Exception e)
			{
				System.err.println("savePackingWeightFileToLocalPDF error0"+e.getMessage());
			}finally{
			fos.close();
			is.close();
			
   	      }
		 
		 System.err.println("savePackingWeightFileToLocalPDF finished");
		 //PDF 转为TXT文件
		 PDFtoTXT(pdf);
		 //TXT文件提取关键数据  SKU CODE 和 Quantity
		 File txtfilie=new File(pdf.getName().split("\\.")[0] + ".txt");
		 BufferedReader reader= null;
		 //抓取完毕的TXT
		 String newSimpleTxtfileFullname="tmp_packing_sheetCartonWeight_list"+getUniqeDateTime()+".txt";
		 File xls = new File(newSimpleTxtfileFullname);
		 BufferedWriter output = new BufferedWriter(new FileWriter(xls));
		 try{
			 
			 reader= new BufferedReader(new FileReader(txtfilie));
			 String dataline=null;
			 int line=1;
			//一次读取一行
			 RegExProcessor processor =  new RegExProcessor();
			 RegExProcessor procWeight =  new RegExProcessor();
			 RegExProcessor proc2 =  new RegExProcessor();
			 RegExProcessor procEan =  new RegExProcessor();
			
				
			 
			// processor.setPatternString("[0-9]{6}[A-Z]{1}[0-9A-Z[\\s]]{3}[0-9A-Z]{0,3}");
			 processor.setPatternString("[0-9]{11}");
			 procWeight.setPatternString("\\d\\.\\d");
			 String cartonNo="";
			 String grossWeight="";
			 String netWeight="";
			 boolean foundColis=false;
			 boolean foundExpedition=false;
			 
			 while((dataline=reader.readLine())!=null){
				 //
				 if(dataline.length()<8)
					 continue;
				 processor.setMatchString(dataline);
				 procWeight.setMatchString(dataline);

				 if(processor.isPatternMatch()){
					 cartonNo=dataline;
					
					 System.err.println("cartonNo ="+cartonNo);
					 output.write(cartonNo+"#");
					 continue;
				 }  
				 if(procWeight.isPatternMatch()){
					// System.err.println("procWeight ="+procWeight);
					 String[] data = dataline.split(" ");
					 for(int i=0;i<data.length;i++){
					    procWeight.setMatchString(data[i]);
						    if(procWeight.isPatternMatch()){
						    	grossWeight=data[i];
						    	netWeight=data[i+1];
						    	//System.err.println("grossWeight="+grossWeight);
								//System.err.println("netWeight="+netWeight);
						    	output.write(grossWeight+"#"+netWeight+"\r\n");
						    	break;
						    }  
					    }
					 }

					 
					
					
				 
			 }
			 reader.close();
			 output.close();
		 
		 }catch(Exception e)
		{
				System.err.println("savePackingWeightFileToLocalPDF error1"+e.getMessage());
			}finally{
				if(reader!=null){
					try{
						reader.close();
					}catch(Exception e)
					{
						System.err.println("savePackingWeightFileToLocalPDF error2"+e.getMessage());
					}
				
				}
				if(output!=null){
					try{
						output.close();
					}catch(Exception e)
					{
						System.err.println("savePackingWeightFileToLocalPDF error3"+e.getMessage());
					}
				}
				
		}
			
			
		 return newSimpleTxtfileFullname;
		 

   }
    
    
    
    //将装修单PDF内容写入txt
    
    public String savePackingFileToLocalPDF(InputStream is)throws IOException, RowsExceededException, WriteException{
		 String newfileFullname="tmp_packing_sheetCartonPDF"+getUniqeDateTime()+".PDF";
		 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		 File pdf = new File(newfileFullname);
		 //System.err.println("123");
		 if(!pdf.createNewFile())
			 return null;
		 FileOutputStream fos=null;
		 try{
			 
			 fos = new FileOutputStream(newfileFullname);
			 
		 }catch(Exception e)
			{
				System.err.println("savePackingFileToLocalPDF error"+e.getMessage());
			}
		 
		 int ch =0;
		 try{
			 
			 while((ch=is.read())!=-1){
				 fos.write(ch);
			 }
			 
		 }catch(Exception e)
			{
				System.err.println("savePackingFileToLocalPDF error0"+e.getMessage());
			}finally{
			fos.close();
			is.close();
			
    	 }
		 
		 System.err.println("savePackingFileToLocalPDF finished");
		 //PDF 转为TXT文件
		 PDFtoTXT(pdf);
		 //TXT文件提取关键数据  SKU CODE 和 Quantity
		 File txtfilie=new File(pdf.getName().split("\\.")[0] + ".txt");
		 BufferedReader reader= null;
		 //抓取完毕的TXT
		 String newSimpleTxtfileFullname="tmp_packing_sheetCarton_list"+getUniqeDateTime()+".txt";
		 File xls = new File(newSimpleTxtfileFullname);
		 BufferedWriter output = new BufferedWriter(new FileWriter(xls));
		 try{
			 
			 reader= new BufferedReader(new FileReader(txtfilie));
			 String dataline=null;
			 int line=1;
			//一次读取一行
			 RegExProcessor processor =  new RegExProcessor();
			 RegExProcessor proc1 =  new RegExProcessor();
			 RegExProcessor proc2 =  new RegExProcessor();
			 RegExProcessor procParcelNumber =  new RegExProcessor();
			 RegExProcessor procEan =  new RegExProcessor();
			
				
			 //processor.setPatternString("[0-9]{6}[0-9A-Z[\\s]]{4}[0-9A-Z]{0,3}");
			 processor.setPatternString("[0-9]{6}[A-Z]{1}[0-9A-Z[\\s]]{3}[0-9A-Z]{0,3}");
		     proc1.setPatternString("dition[\\s]");
		     //法文版关键字是Colis
			 proc2.setPatternString("N*[\\s]Colis[\\s]");
			 //英文版
			 procParcelNumber.setPatternString("Parcel[\\s]number[\\s]");
			 procEan.setPatternString("[0-9A-Z][0-9]{11,12}");
			 String Colis="";
			 String expedition="";
			 String ParcelNumber="";
			 boolean foundColis=false;
			 boolean foundExpedition=false;
			 boolean foundParcelNumber=false;
			 
			 while((dataline=reader.readLine())!=null){
				 //
				 if(dataline.length()<13)
					 continue;
				 processor.setMatchString(dataline.substring(0,13));
				 proc1.setMatchString(dataline);
				 proc2.setMatchString(dataline);
				 procParcelNumber.setMatchString(dataline);
				 procEan.setMatchString(dataline);
				 if(proc1.isPatternMatch()){
					 expedition=dataline;
					 expedition=expedition.substring(expedition.lastIndexOf(':')+1);
					 System.err.println("expedition ="+expedition);
					 if(foundExpedition==false){
						 foundExpedition=true;
						 output.write(expedition+"#");
					 }
				 }  
				 if(proc2.isPatternMatch()){
					 Colis=dataline;
					 Colis=Colis.substring(Colis.lastIndexOf(' ')+1);
					 System.err.println("Colis Code="+Colis);
					 if(foundColis==false){
						 foundColis=true;
						 output.write(Colis+"\r\n");
					 }
				 }
				 else if(procParcelNumber.isPatternMatch()){
					 ParcelNumber=dataline;
					 ParcelNumber=ParcelNumber.substring(ParcelNumber.lastIndexOf(' ')+1);
					 System.err.println("Parcel Number="+ParcelNumber);
					 if(foundParcelNumber==false){
						 foundParcelNumber=true;
						 //英文版本的没有expedition,所以要添加一个分隔符#
						 if(foundExpedition==false)
							 output.write("#"+ParcelNumber+"\r\n");
						 else
							 output.write(ParcelNumber+"\r\n");
					 }
				 }
				 
				 
				 String ean=procEan.getPureData();
				 if(processor.isPatternMatch()&&procEan.isPatternMatch()){
					 String skucode="";
					 String qty="";
					 skucode=processor.getPureData();
					 qty=dataline.substring( dataline.lastIndexOf(' ')+1);
					 skucode=skucode.trim();
					 System.err.println("Sku Code="+skucode);
					 System.err.println("Ean="+ean);
					 System.err.println("qty ="+qty);
					 output.write(skucode+","+qty+"\r\n");
				 }
				 
				 
				 
				 line++;
				 
			 }
			 reader.close();
			 output.close();
		 
		 }catch(Exception e)
		{
				System.err.println("savePackingFileToLocalPDF error1"+e.getMessage());
			}finally{
				if(reader!=null){
					try{
						reader.close();
					}catch(Exception e)
					{
						System.err.println("savePackingFileToLocalPDF error2"+e.getMessage());
					}
				
				}
				if(output!=null){
					try{
						output.close();
					}catch(Exception e)
					{
						System.err.println("savePackingFileToLocalPDF error3"+e.getMessage());
					}
				}
				
		}
			
			
		 return newSimpleTxtfileFullname;
		 

    }
    
    
	
	
	//1111111111111111111111111111
	 public String savePackingFileToLocal(InputStream is)throws IOException, RowsExceededException, WriteException{
		 String newfileFullname="tmp_packing_sheet"+getUniqeDateTime()+".txt";
		 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
		 File xls = new File(newfileFullname);
		 //System.err.println("123");
		 if(!xls.createNewFile())
			 return null;
		 
		 FileOutputStream fos = null;
		 BufferedWriter output = new BufferedWriter(new FileWriter(xls));
	       
		 try{
			   fos = new FileOutputStream(xls);
			   int bytesRead;
			   byte[] buf = new byte[4 * 1024]; //4K buffer
			   RegExProcessor processor =  new RegExProcessor();
			   RegExProcessor proc2 =  new RegExProcessor();
			   proc2.setPatternString("N*[\\s]COLIS[\\s]");
			   String dataline="";
			   String LastcartonCode="-1";
			   String CurcartonCode="";
			   int lastSharpLine=-2;
			   int thisdataLine=0;
			   while((dataline = bufferedReader.readLine()) != null){
				   //System.err.println(buf);
				   proc2.setMatchString(dataline);
				   
				 if(proc2.isPatternMatch()){
					 CurcartonCode=dataline;
					 System.err.println("CurcartonCode Code="+CurcartonCode);
				 }
				   
				//System.err.println(dataline);
				processor.setMatchString(dataline);
				
				try{
					processor.setPatternString("Quantit");
					if(processor.isPatternMatch()){
						if(!CurcartonCode.equals(LastcartonCode)){
							System.err.println("match Quantit");
							String ss="#\r\n";
							 thisdataLine++;
							if(lastSharpLine<thisdataLine-1){
							output.write(ss);
							lastSharpLine=thisdataLine;
							//新的carton开始
							
							}
							LastcartonCode=CurcartonCode;
						}
					}
						
					else
						{
						
						 processor.resetPatternString();
						 if(processor.isPatternMatch()){
							 //开始装箱
							 thisdataLine++;
							// System.err.println(dataline);
								//System.err.println("match data");
								String puredata=processor.getPureData();
								
								processor.setPatternString("[0-9]{6}[0-9A-Z[\\s]]{4,7}[\\s]");
								processor.setMatchString(puredata);
								String skucode=processor.getPureData();
								skucode=skucode.substring(0,skucode.length()-1);
							//	System.err.println("skucode data="+skucode);
								
								processor.setPatternString("[0-9A-Z][0-9]{11,12}");
								processor.setMatchString(puredata);
								String ean=processor.getPureData();
							//	System.err.println("ean data="+ean);
								
							 
								String quantity=puredata.substring(skucode.length()+ean.length()+2);
							//	System.err.println("quantity data="+quantity);
								
								//output.write(puredata+"\r\n");
								output.write(skucode+","+quantity+"\r\n");
							}
						}
				}
				catch(Exception e)
				{
					System.err.println("savePackingFileToLocal error"+e.getMessage());
				}
			   
			   }
			   bufferedReader.close();
			   output.close();
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
	
	 
	 private String getFileName(String csvFileName){
		  return csvFileName.substring(0, csvFileName.length()-4)+".txt";
		 }
	 
	
	
	

	public static void main(String[] args){
		  
		rt.RegExProcessor processor =  RegExProcessor.getInstance();
		processor.setPatternString(processor.patternString);
		processor.setMatchString("W DRESS KNIT BACK 02 NOIR 38 222800DC0238 3606555924661 1");
		try{
			if(processor.isPatternMatch())
				System.err.println("match");
			else
				System.err.println("not match");
		}
		catch(Exception e)
		{}
		   
	}
}



