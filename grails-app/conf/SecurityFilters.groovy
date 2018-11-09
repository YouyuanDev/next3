class SecurityFilters {
	def filters = {
		loginCheck(controller: '*', action: '*') {
			before = {
				if (!session.user && actionName != "login") {
					redirect(controller: "auth", action: "login")
					//return true
				}
			}
		}
	}
}
