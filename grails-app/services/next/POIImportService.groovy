package next

import org.apache.poi.hssf.usermodel.*

class POIImportService extends ImportService {
  def process(FileInputStream is) {
    //Workbook wb = WorkbookFactory.create(in)
    HSSFWorkbook wb = new HSSFWorkbook(is);
    HSSFSheet sheet = wb.getSheetAt(0)
    def cell
    int rowNum = sheet.getLastRowNum()
    for (int i = 0; i <= rowNum; i++) {
      def row = sheet.getRow(i)
      cell = row.getCell((short) 0)
      log.info cell.getStringCellValue()
      break
    }
  }
}