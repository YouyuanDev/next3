package next


class StandardReference {
	String productLabelNameZh
	String executeStandard
	String validDate
	String saftyStandardType
	String remark
	
	    
	
    static constraints = {
		
		productLabelNameZh(maxSize: 500,unique: true, nullable: false)
		executeStandard(maxSize: 200, nullable: true)
		validDate(maxSize: 200, nullable: true)
		saftyStandardType(maxSize: 200, nullable: true)
		remark(maxSize: 200, nullable: true)
		
    }
}
