serverAdapter = new function(){
	this.ajaxCall = function(url, type, data, onSuccess){
		$.ajax({
			url: url,
			type: type,
			dataType: 'json',
			data: data,
			success: onSuccess,
			error: function(e){
				toast(e);
				console.error(e);
			}
		});
	}; //ajaxCall
	
	this.getRegisteredDatabases = function(callback){
		//TODO
	} //getRegisteredDatabases
}; //serverAdapter