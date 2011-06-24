$(document).ready(
		function() {
			$("button, input:submit, a", ".button").button();
			$("#search-button").button();
			
			$('#relations').tabs({
				collapsible: true,
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
			
			//hide message_body after the first one
			$(".listItems .list-item-body").hide();
			
			//toggle message_body
			$(".list-item-head").click(function(){
				$(this).next(".list-item-body").slideToggle(500)
				return false;
			});
			$(".collapase-all-items").click(function(){
				$(".list-item-body").slideUp(500)
				return false;
			});
			
			$('#edit-tabs').tabs({
				collapsible: true,
				 spinner: 'Retrieving data...' ,
				   ajaxOptions: {
				       error: function( xhr, status, index, anchor ) {
					       $( anchor.hash ).html(
						       "Couldn't load this tab. We'll try to fix this as soon as possible." );
				    }
			   }
		    }).find( ".ui-tabs-nav" ).sortable({ axis: "x" });

			
			$("#change-context").click(function(e) {
				e.preventDefault();
				var url = $("#changeContextForm").attr("action");				
				var data = $("#changeContextForm").serialize();				
				$.ajax({
					type: 'post',
					url: url,
					data: data
				,
				success: function(data) {
					apprise("Context Updated")
				},
				error: function(){
					apprise("Failed to update the Context.");
				},
				});
			});
			
			$("#update-account").click(function(e) {
				
				e.preventDefault();
				var url = $("#accountForm").attr("action");				
				var data = $("#accountForm").serialize();						
				$.ajax({
					type: 'post',
					url: url,
					data: data
				,
				success: function(data) {
					apprise("Account Information Updated.");
				},
				error: function(data){
					apprise("Failed to update the Account Information.");
				},
				});
			});
			$("#change-password").click(function(e) {
				e.preventDefault();
				var url = $("#passwordForm").attr("action");
				alert(url);
				var data = $("#passwordForm").serialize();
				alert(data);
				$.ajax({
					type: 'post',
					url: url,
					data: data
				,
				success: function(data) {
					apprise("Password updated successfully.");
				},
				error: function(data){
					apprise("Failed to update password.");
				},
				});
			});
			
			//Bulk Actions
			$("#deactivate-btn").click(function(e) {	
				e.preventDefault();
				var url =  $(this).find("a").attr("href");
				var data = $('input[class^=key-check]:checked').serialize();				
				/*$('input[name^=keys]:checked').each(function(){
					var id = $(this).attr('value');
					alert(id);
				  });*/
				$.ajax({
					type: 'post',
					url: url,
					data: data
				,
				success: function(data) {
					apprise(data)
				},
				error: function(data){
					if(data == null){
					apprise("Falied to deactivate users");
					}
					apprise(data);
				},
				});
			});
			
			$(".bulk-freeze").click(function(e) {	
				e.preventDefault();
				var url =  $(this).find("a").attr("href");														
				var data = $('input[class^=key-check]:checked').serialize();			
				$.ajax({
					type: 'post',
					url: url,
					data: data
				,
				success: function(data) {
					/** update the attribute later
					 * $('input[class^=key-check]:checked').each(function(){
						var currentv
						if($(this).attr('value',)
						$(this).attr('value', 'true');
						
					  });**/
					apprise(data)
				},
				error: function(data){
					apprise(data);
				},
				});
			});	
			
			/** this is similar to anyother bulk action but purposfully has been kep separate*/
			$(".bulk-delete").click(function(e) {	
				e.preventDefault();
				var url =  $(this).find("a").attr("href");														
				var data = $('input[class^=key-check]:checked').serialize();			
				$.ajax({
					type: 'post',
					url: url,
					data: data
				,
				success: function(data) {
					/** update the attribute later
					 * $('input[class^=key-check]:checked').each(function(){
						var currentv
						if($(this).attr('value',)
						$(this).attr('value', 'true');
						
					  });**/
					apprise(data)
				},
				error: function(data){
					apprise(data);
				},
				});
			});		 			
}); //End of document ready
