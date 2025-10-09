# 2h chrono pour traquer tout ce qui bouge dans ton backend avec OpenTelemetry

Tu déploies des services back en production, mais dès qu’un bug survient ou qu’une latence apparaît, c’est la chasse au trésor dans les logs ? Tu te demandes comment avoir une vision claire de ce que fait ton application, sans transformer ton code en sapin de Noël 🌲 ?

Ce workshop est fait pour toi.

En 2h chrono, on part de zéro — aucune observabilité en place — pour instrumenter une application Spring Boot avec OpenTelemetry, sans modifier ton code (ou presque).
L’objectif : observer en temps réel les erreurs, les latences, les appels inter-services, et même obtenir des insights métier depuis les traces !

🧭 Au programme :  
&nbsp; ⚙️ Mise en place d’un environnement local (ou cloud) avec une app Spring sans observabilité  
&nbsp; 🕵️ Instrumentation automatique avec l’agent Java OpenTelemetry  
&nbsp; 📡 Export des données vers un stack OSS : SigNoz  
&nbsp; 🔍 Exploration des traces, erreurs et temps de réponse  
&nbsp; 📊 Ajout de custom spans pour injecter de la donnée métier  
&nbsp; 🎯 Analyse : où sont les goulets d’étranglement ?

💻 Pré-requis :

- Session sur l'OS avec les droits administrateur
- IDE comme IntelliJ ou Visual Studio Code, supportant Java
- PowerShell 7 ou Bash
- Docker
- Git et un compte [GitHub](https://github.com/)
- [mise](https://mise.jdx.dev/getting-started.html)
- [Headlamp](https://headlamp.dev/)
