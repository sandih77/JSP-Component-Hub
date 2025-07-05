<%@ page import="gui.*" %>
<%@ page import="data.SaveData" %>
<%@ page import="java.lang.reflect.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String className = request.getParameter("class");
    Object obj = null;

    if (className != null) {
        try {
            Class<?> clazz = Class.forName(className);
            obj = clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String nomChamp = field.getName();
                String valeurParam = request.getParameter(nomChamp);

                if (valeurParam == null) continue;

                Class<?> type = field.getType();

                try {
                    if (Deroulante.class.isAssignableFrom(type)) {
                        Deroulante deroulante = (Deroulante) type.getDeclaredConstructor().newInstance();
                        deroulante.setValeurSelectionnee(valeurParam);
                        field.set(obj, deroulante);
                    } 
                    else if (type == int.class || type == Integer.class) {
                        field.set(obj, Integer.parseInt(valeurParam));
                    } else if (type == boolean.class || type == Boolean.class) {
                        field.set(obj, valeurParam.equalsIgnoreCase("true") || valeurParam.equalsIgnoreCase("on"));
                    } else if (type == String.class) {
                        field.set(obj, valeurParam);
                    }
                } catch (Exception e) {
                    e.printStackTrace(); 
                }
            }

            String path = application.getRealPath("/data/donnees.txt");
            SaveData sd = new SaveData();
            sd.save(path, obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Enregistrement terminé</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <%
        if (obj != null) {
    %>
    <div class="alert alert-success">
        <h4 class="alert-heading">Données enregistrées avec succès !</h4>
    </div>

    <div class="card shadow">
        <div class="card-header">
            <strong>Détails :</strong>
        </div>
        <ul class="list-group list-group-flush">
            <%
                for (Field field : obj.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        Object valeur = field.get(obj);
            %>
                <li class="list-group-item">
                    <strong><%= field.getName() %> :</strong> <%= valeur %>
                </li>
            <%
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            %>
        </ul>
    </div>

    <div class="mt-4">
        <a href="index.jsp" class="btn btn-primary">← Retour au formulaire</a>
    </div>

    <%
        } else {
    %>
    <div class="alert alert-danger">
        <h4 class="alert-heading">❌ Erreur :</h4>
        <p>Classe non reconnue ou instanciation impossible.</p>
        <a href="index.jsp" class="btn btn-danger mt-3">← Retour au formulaire</a>
    </div>
    <%
        }
    %>
</div>

</body>
</html>
