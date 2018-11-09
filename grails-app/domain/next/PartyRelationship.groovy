package next
class PartyRelationship {
  Party partyFrom
  Role roleFrom
  Party partyTo
  Role roleTo
  String type
  Date dateCreated
  Date lastUpdated
  /*
    static constraints = {
        PartyRelationship(unique:['partyFrom','roleFrom','partyTo','roleTo','type'])
    }
    */
}
