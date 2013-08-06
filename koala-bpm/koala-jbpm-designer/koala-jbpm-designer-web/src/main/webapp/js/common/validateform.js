
	function validateform(form) {
  	  $.metadata.setType("attr", "validate");
      var formvalidator = form.validate({
          errorPlacement: function (lable, element) {

              if (element.hasClass("l-textarea")) {
                  element.addClass("l-textarea-invalid");
              }
              else if (element.hasClass("l-text-field")) {
                  element.parent().addClass("l-text-invalid");
              }

              var nextCell = element.parents("td:first").next("td");
              nextCell.find("div.l-exclamation").remove(); 
              $('<div class="l-exclamation" title="' + lable.html() + '"></div>').appendTo(nextCell).ligerTip(); 
          },
          success: function (lable) {
              var element = $("#" + lable.attr("for"));
              var nextCell = element.parents("td:first").next("td");
              if (element.hasClass("l-textarea")) {
                  element.removeClass("l-textarea-invalid");
              }
              else if (element.hasClass("l-text-field")) {
                  element.parent().removeClass("l-text-invalid");
              }
              nextCell.find("div.l-exclamation").remove();
          }
      });
      
      return form.valid();
	}
