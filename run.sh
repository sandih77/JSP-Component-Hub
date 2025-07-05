clear

echo "Compilation...";

rm -r WEB-INF/classes

if javac -cp /home/sandih/studies/Java/tomcat/lib/servlet_api.jar -d WEB-INF/classes src/*.java; then
    echo "Aucune erreur"
else 
    echo "Erreur de compilation"
fi