<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<% @CODEPAGE="65001" language="vbscript" %>
<% session.CodePage = "65001" %>
<% Response.CharSet = "utf-8" %>
<% Response.buffer=true %>
<% Response.Expires = 0 %>
<!--#include virtual="/common/include/func_db.asp"-->

<%
	firstDep = "06"
	dispNum = "0604"

	Function fnbWebZineHosuNm()
	 dim szScriptName__, arrSzScriptDir__,szStr
	 szScriptName__ =lcase(request.servervariables("SCRIPT_NAME"))
	 arrSzScriptDir__ =  Split(szScriptName__,"/")
	 
	 If UBound(arrSzScriptDir__) > 2 Then 
	  szStr = arrSzScriptDir__(2)
	 Else 
	  szStr = ""
	 End If 
	 
	 fnbWebZineHosuNm = szStr
	End Function 

	Function fnbWebZineUserIp()
	 userip = request.servervariables("REMOTE_ADDR")
	 fnbWebZineUserIp = userip
	end function

	''	HOSU write	
	Dim szHosu : szHosu = fnbWebZineHosuNm()	
	''response.write szHosu

	''	USERIP write
	Dim userip : userip = fnbWebZineUserIp()	
	''response.write userip

	
										

	insertsql = 			" INSERT INTO SEAH_BOARD_WEBZIN_LOG																							"	&_
							" (																															"	&_
							"	B_WEBZIN_SEQ, YYYY, YYYY_MM , YYYY_MM_DD, REGDATE, HOSU, USERIP															"	&_
							" )																															"	&_
							" VALUES																													"	&_
							" (																															"	&_
							"	B_WEBZIN_AUTO_SEQ.NEXTVAL, TO_NUMBER(TO_CHAR(sysdate,'YYYY')), TO_NUMBER(TO_CHAR(sysdate,'YYYYMM')), TO_NUMBER(TO_CHAR(sysdate,'YYYYMMDD')), sysdate, '" & szHosu		& "' , '" & userip		& "' ) "

							
	dbOpen()
	oCon.BeginTrans
	oCon.execute(insertsql)
	oCon.CommitTrans
	dbClose()
%>


<%
	Dim objFSO, objTextFile 
	Dim objCreatedFile, objOpenedFile

	Const ForReading = 1, ForWriting = 2, ForAppending = 8

	'Create the FSO.
	Set objFSO = CreateObject("Scripting.FileSystemObject")

	' Open file for reading.
	Set objTextFile = objFSO.OpenTextFile("c:\test\Log.txt", ForAppending)
	'Set objTextFile = objFSO.CreateTextFile("c:\test\Log.txt", True)

	' Write a line with a newline character.
	objTextFile.WriteLine("ID = "&id)

	' Write a line.
	objTextFile.Write ("CONTENT= "&content)

	' Write a line.
	objTextFile.Write ("HOSU : " & date() & " " &time()) 

	' Write three newline characters to the file.
	objTextFile.WriteBlankLines(3)

	objTextFile.Close
%>