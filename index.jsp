<%@ page import="gui.Personne" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>JSP COMPONENT HUB</title>
</head>
<body>
    <h2>Formulaire</h2>
    <%
        Personne p = new Personne();
        String html = p.construireHtmlFormulaire();
        out.println(html);
    %>
</body>
</html>
