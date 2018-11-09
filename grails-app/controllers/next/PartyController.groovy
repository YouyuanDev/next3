package next
class PartyController {
  def beforeInterceptor = [action: this.&checkUser, except: ['index', 'list', 'show']]
  def scaffold = true

  def checkUser() {
    if (!session.user) {
      // i.e. user not logged in
      redirect(controller: 'userLogin', action: 'login')
      return false
    }
  }
}
