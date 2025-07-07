<%@ page import="java.util.*" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page import="data.ReadData" %>
<%@ page import="gui.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String cheminFichier = application.getRealPath("/data/donnees.txt");

    AutoPersonne composant = new AutoPersonne();

    ReadData lecteur = new ReadData();
    List<Object> listeDonnees = null;
    try {
        listeDonnees = lecteur.getAllData(cheminFichier, AutoPersonne.class);
    } catch (Exception e) {
        e.printStackTrace();
    }

    composant.setData(listeDonnees);

    Liste listeComposant = new Liste(listeDonnees);

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String action = "index.jsp";

        composant.construireHtmlFormulaire(paramMap, cheminFichier, action);

        response.sendRedirect("index.jsp");
        return;
    }

    String formulaireHtml = composant.construireHtmlFormulaire(null, cheminFichier, "index.jsp");
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <title>Exemple Composant Formulaire</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
    <div class="container mt-4">
        <%= formulaireHtml %>

        <%
            if (listeComposant.getData() != null && !listeComposant.getData().isEmpty()) {
                out.println(listeComposant.construireHtmlTable());
            } else {
        %>
            <p class="mt-3">Aucune donnée à afficher.</p>
        <% } %>
    </div>
</body>
</html>
