<%@ page import="gui.*" %>
<%@ page import="data.ReadData" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <title>JSP-COMPONENT-HUB</title>
</head>
<body>
    <%
        Personne p = new Personne();
        String action = "traitementData.jsp";

        out.println(p.construireHtmlFormulaire(action));

        try {
            String path = application.getRealPath("/data/donnees.txt");
            ReadData reader = new ReadData();
            List<Object> list = reader.getAllData(path, Personne.class);

            Liste composantListe = new Liste(list);
            out.println(composantListe.construireHtmlTable());

        } catch (Exception e) {
            out.println("<pre>Erreur : " + e.getMessage() + "</pre>");
        }
    %>
</body>
</html>
