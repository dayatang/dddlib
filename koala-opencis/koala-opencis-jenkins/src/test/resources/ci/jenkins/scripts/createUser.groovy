package ci.jenkins.scripts

import hudson.model.*
import ci.jenkins.model.*

User user = User.get({0}, true)
user.save()
println user.getACL()