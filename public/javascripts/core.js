$(document).ready(
		function() {
			$("button, input:submit, a", ".button").button();
			$("#search-button").button();
			$(".check-all").click(
					function() {
						if ($(this).is(":checked")) {
							$(this).closest("table").find("input.key-check")
									.attr("checked", "checked");
						} else {
							$(this).closest("table").find("input.key-check")
									.removeAttr("checked");
						}
					});
			$('#relations').tabs({
				 spinner: 'Retrieving data...' ,
				   ajaxOptions: {
				       error: function( xhr, status, index, anchor ) {
					       $( anchor.hash ).html(
						       "Couldn't load this tab. We'll try to fix this as soon as possible." );
				    }
			   }
		    }).find( ".ui-tabs-nav" ).sortable({ axis: "x" });
			
			$('#account-actions').menu({ 
				content: $('#account-actions').next().html(), // grab content from this page
				showSpeed: 100 
			});
		
			// Actions defined

			$(".action-delete").click(function(e) {
				e.preventDefault();
				var parent =  $(this).parents(".listItem");
				var url = $(this).attr("href");
				var id = $(".list-item-id").val();	
				apprise("Are u sure?",{'confirm':true}, function(r){
					if(r){	    		    			    
						$.ajax({
							type: 'delete',
							url: url,	    		
							beforeSend: function() {
							parent.animate({ backgroundColor: "#fbc7c7" }, "fast")
							.animate({ opacity: "hide" }, "slow")
						},
						success: function(data) {

							parent.slideUp(300,function() {
								$(this).parents(".listItem").remove();
							});
							apprise(data);
						}
						});
					}else{
						return false;
					}});
			});

			$(".action-freeze").click(function(e) {
				e.preventDefault();
				var parent = $(this).parents(".listItem");
				var url = $(this).attr("href");
				apprise("Are u sure?", {
					'confirm' : true
				}, function(r) {
					if (r) {
						$.ajax({
							type: 'post',
							url: url,	    		
							beforeSend: function() {
							parent.animate({ backgroundColor: "#fff568" }, "fast")
							.animate({ backgroundColor: "#ffffff" }, "slow")
						},
						success: function(data) {
							apprise(data);
						}
						});
					} else {
						return false;
					}
				});
			});

			$(".action-boolean").click(function(e) {
				e.preventDefault();
				var parent = $(this).parents(".listItem");
				var url = $(this).attr("href");
				apprise("Are u sure?", {
					'confirm' : true
				}, function(r) {
					if (r) {
						$.ajax({
							type: 'post',
							url: url,	    		
							beforeSend: function() {
							parent.animate({ backgroundColor: "#fff568" }, "fast")
							.animate({ backgroundColor: "#ffffff" }, "slow")
						},
						success: function(data) {
							apprise(data);
						}
						});
					} else {
						return false;
					}
				});
			});
			
			$(".action-revise").click(function(e) {
				e.preventDefault();
				var parent = $(this).parents(".listItem");
				apprise("Will be implemented soon.");
			});
			
			$(".action-clone").click(function(e) {
				e.preventDefault();
				var parent = $(this).parents(".listItem");
				apprise("Will be implemented soon.");
			});
			
			
}); //End of document ready
