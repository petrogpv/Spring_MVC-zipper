<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html><head><body>
<h1>${answer} </h1>

<c:if test="${key ne null}">
    <p><strong>Upload and zip successfully next files:</strong></p>
    <c:set var="count" value="0" scope="page" />

    <c:forEach items="${names}" var="name">
        <c:set var="count" value="${count + 1}" scope="page"/>
        <p>${count}. <b>${name}</b> </p>
    </c:forEach>

    <p>Zip weight: <b>${size}</b> Bytes</p>

    <input type="submit" value="Dowload ZIP" onclick="window.location='/download/${key}';"/>
    <br>
</c:if>

<p><strong>Choose files to zip:</strong></p>

<form action="/common" method="post" enctype="multipart/form-data">
<input type="file" name="file"/>
    <br><input type="file" name="file"/>
    <br><input type="file" name="file"/><br>
    <br> <input type="submit" value="Zip it!" style="height:50px;width:150px"/>
</form>
</body></head></html>