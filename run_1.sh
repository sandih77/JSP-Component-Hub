#!/bin/bash

clear
echo "🔧 Compilation en cours..."

rm -rf WEB-INF/classes

if javac -cp "/home/kali/tomcat/lib/servlet_api.jar" -d WEB-INF/classes src/gui/*.java src/data/*.java src/tools/*.java; then
    echo "✅ Aucune erreur de compilation"
else 
    echo "❌ Erreur de compilation"
    exit 1
fi

echo ""
echo "🔁 Redémarrage de Tomcat..."

if sudo /home/kali/tomcat/bin/shutdown.sh; then
    echo "✅ Tomcat arrêté avec succès"
    sleep 2
    if sudo /home/kali/tomcat/bin/startup.sh; then
        echo "✅ Tomcat redémarré avec succès"
    else
        echo "❌ Échec du démarrage de Tomcat"
    fi
else 
    echo "❌ Échec de l'arrêt de Tomcat"
fi
