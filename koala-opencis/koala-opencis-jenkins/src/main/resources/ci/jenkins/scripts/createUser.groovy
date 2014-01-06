
hudson.model.User user = hudson.model.User.get(userId, true)

user.save()
println user.getACL()