
$("input").focus(function(){
    $(this).addClass("glowing");
});
$("input").blur(function(){
    $(this).removeClass("glowing");
});

$("footer a").focus(function(){
	$(this).css("text-decoration","none");
	$(this).css("color","#e67777");
});

$("form a").focus(function(){
	$(this).css("text-decoration","none");
	$(this).css("color","white");
});

$("#regest").click(function(){
	$.ajax({
		type: 'POST',
		url: "/regester",
		data: $("#fm").serialize(),
		error: function(request){
			alert("Connection error");
		},
		success: function(data){
			alert(data);
			window.location.href = "#";
		},
		
	});
});