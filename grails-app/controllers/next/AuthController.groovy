package next
class AuthController {
  def login = {
    log.info request.get
    if (request.get) {
      render(view: "login")
    } else {
      log.info params
      def userLogin = User.findWhere(login: params['login'], password: params['password'])
	  log.info "UserLogin="+userLogin
      session.user = userLogin
      if (userLogin) {
        //redirect(uri:"main.gsp")
        render(text: "{success:true}")
      } else {
        //flash.message = "Login Error!"
        /*redirect(controller:'userLogin',action:'login')*/
        //redirect(action:login)
        render(text: "{success:false,errors:{reason:'Login failed.Try again'}}")
      }
    }
  }
	def logout = {
		log.info "User agent: " + request.getHeader("User-Agent")
		session.invalidate()
		redirect(action: "login")
	}

	def main = {
		redirect(uri:"main.gsp")
		return true
	}
}
