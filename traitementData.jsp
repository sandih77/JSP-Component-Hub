<%@ page import="gui.Personne" %>
<%@ page import="data.SaveData" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // 1. Récupération des données du formulaire
    String nom = request.getParameter("nom");
    String prenom = request.getParameter("prenom");
    String sexe = request.getParameter("sexe");
    String ageStr = request.getParameter("age");
    String adress = request.getParameter("adress");

    int age = 0;
    try {
        age = Integer.parseInt(ageStr);
    } catch (Exception e) {
        // ignore ou log
    }

    // 2. Création de l'objet Personne
    Personne p = new Personne();
    p.setNom(nom);
    p.setPrenom(prenom);
    p.setSexe(sexe);
    p.setAge(age);
    p.setAdress(adress);

    // 3. Sauvegarde via SaveData
    String path = application.getRealPath("/data/donnees.txt");
    SaveData sd = new SaveData();
    sd.save(path, p);
%>

<html>
<head>
    <title>Enregistrement terminé</title>
</head>
<body>
    <h2>Données enregistrées avec succès !</h2>
    <ul>
        <li><strong>Nom :</strong> <%= nom %></li>
        <li><strong>Prénom :</strong> <%= prenom %></li>
        <li><strong>Sexe :</strong> <%= sexe %></li>
        <li><strong>Âge :</strong> <%= age %></li>
        <li><strong>Adresse :</strong> <%= adress %></li>
    </ul>
    <a href="index.jsp">← Retour au formulaire</a>
</body>
</html>
