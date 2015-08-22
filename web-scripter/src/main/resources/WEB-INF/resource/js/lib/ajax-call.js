function ajaxCall(url, type, data, onSuccess){
	$.ajax({
			url: url,
			type: type,
			dataType: 'json',
			data: data,
			success: onSuccess,
			error: function(e){
				bootbox.alert(JSON.stringify(e));
				console.error(e);
			}
		});
} //ajaxCall