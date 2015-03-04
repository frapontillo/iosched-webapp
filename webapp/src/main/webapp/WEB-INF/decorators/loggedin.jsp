<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored ="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<!DOCTYPE html>
<html>
<head>
    <title><decorator:title default="Conference Engineer"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Funky Android Ltd.">
    <meta name="theme-color" content="#2980B9">
    <c:set var="contextPath" value="${pageContext.request.contextPath}"/>
    <link rel="stylesheet" type="text/css" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/style.css'/>" >
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
	<decorator:head />
</head>
<body>
<div id="wrap">
    <decorator:body/>
</div>
<div id="footer">
    <div class="container">
        <p class="text-center">Website and back-end software : &copy;Copyright 2013-2015 <a target="_blank" href="http://funkyandroid.com/">Funky Android Ltd.</a>, All rights reserved.</p>
    </div>
</div>
</body>
</html>
