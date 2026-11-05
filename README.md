# API de calcul

Cette API en Kotlin et Spring Boot effectue des calculs sur deux nombres. Le résultat est un nombre JSON. Swagger UI permet d'essayer chaque opération dans le navigateur.

## Exécution

Installer JDK 17 ou une version plus récente et Git. Au premier lancement, le script Gradle télécharge Gradle et les dépendances du projet. Une connexion Internet est donc nécessaire.

```sh
git clone https://github.com/thejosht/seg3502-lab6-calculator.git
cd seg3502-lab6-calculator
./gradlew bootRun
```

Sur Windows, utiliser `.\gradlew.bat bootRun`. Ouvrir [Swagger UI](http://localhost:8080/swagger-ui.html). L'adresse racine redirige aussi vers cette page. Le serveur n'écoute que sur cet ordinateur.

Si le port 8080 est déjà utilisé:

```sh
./gradlew bootRun --args='--server.port=8081'
```

Remplacer le port dans les adresses ci-dessous par celui choisi.

## Opérations

Les deux paramètres dans l'URL sont des nombres qui peuvent être négatifs ou décimaux. Utiliser un point comme séparateur décimal, par exemple `2.5`.

| Méthode | Route | Exemple de résultat |
| --- | --- | --- |
| GET | `/calculator/add/10/2` | `12.0` |
| GET | `/calculator/subtract/10/2` | `8.0` |
| GET | `/calculator/multiply/10/2` | `20.0` |
| GET | `/calculator/divide/7/2` | `3.5` |

Chaque route suit le format `/calculator/{operation}/{nombre1}/{nombre2}`. La soustraction calcule `nombre1 - nombre2` et la division calcule `nombre1 / nombre2`.

```sh
curl http://localhost:8080/calculator/add/10/2
curl http://localhost:8080/calculator/subtract/10/2
curl http://localhost:8080/calculator/multiply/10/2
curl http://localhost:8080/calculator/divide/7/2
```

Les nombres invalides, les valeurs non finies, la division par zéro et les dépassements numériques renvoient HTTP 400 avec un court message. Les calculs utilisent le type `Double` et peuvent donc présenter des erreurs d'arrondi liées à la virgule flottante. Aucune base de données ni clé d'API n'est nécessaire.

## OpenAPI

- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [Spécification JSON](http://localhost:8080/v3/api-docs)
- [Spécification YAML](http://localhost:8080/v3/api-docs.yaml)

La spécification est générée à partir du contrôleur. Elle décrit les quatre routes, leurs paramètres, les résultats numériques et les erreurs HTTP 400.

## Tests et compilation

```sh
./gradlew clean test bootJar
java -jar build/libs/calculator-api-0.0.1-SNAPSHOT.jar
```

Les tests appellent un serveur réel sur un port local attribué automatiquement. Ils couvrent les quatre opérations, les valeurs négatives et décimales, les entrées invalides, la division par zéro, les dépassements numériques, la documentation et le routage. Ouvrir `build/reports/tests/test/index.html` pour voir les résultats. Arrêter le serveur avec Ctrl+C.

## Références

- [Documentation Spring Boot](https://docs.spring.io/spring-boot/reference/)
- [Documentation springdoc OpenAPI](https://springdoc.org/)
