function selectionChanged(form)
{
	form.submit();
}

function fireSelectionType(form, type)
{
	form.selectiontype.value=type;
	form.submit();
}

function clearFormElements(form)
{
	for(var i=0; i < (form.elements.length); ++i)
	{
		var temp = form.elements[i];
		if(temp.type == "text" || temp.type == "textarea")
		{
			temp.value = "";
		}
	}
}

function comparePwds(form) {
	
	if(form.password.value!=form.repassword.value) {
		alert("The passwords entered do not match, please re-enter");
		return false;
	}
	else if(form.password.value.length<6) {
		alert("The password must be a minimum of 6 characters long");
		return false;
	}
	else
		form.submit();
}
