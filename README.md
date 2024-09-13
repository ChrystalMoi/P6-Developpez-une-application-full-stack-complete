# P6-Developpez-une-application-full-stack-complete

Ce projet est une application full-stack utilisant Angular pour le front-end et Java pour le back-end.

Le but est de créer le prochain réseau social dédié aux développeurs : MDD (Monde de Dév). Le projet de réseau social MDD est d’aider les développeurs qui cherchent un travail, grâce à la mise en relation, en encourageant les liens et la collaboration entre pairs qui ont des intérêts communs.

## Sommaire

- [P6 - Développez une application full stack complète](#p6---développez-une-application-full-stack-complète)
- [Pré-requis](#pré-requis)
- [Installation du projet](#installation-du-projet)
  - [1. Clonez le dépôt](#1-clonez-le-dépôt)
  - [2. Installation du front-end](#2-installation-du-front-end)
  - [3. Construction et démarrage du front-end](#3-construction-et-démarrage-du-front-end)
  - [4. Installation et configuration de la base de données](#4-installation-et-configuration-de-la-base-de-données)
  - [5. Configuration du back-end](#5-configuration-du-back-end)
    - [Exemple de configuration MySQL classique](#exemple-de-configuration-mysql-classique)
    - [Exemple de configuration MySQL avec variables d'environnement](#exemple-de-configuration-mysql-avec-variables-denvironnement)

## Pré-requis

- **Angular CLI** version 18
- **Java** version 17
- **MySQL**
- **Node.js** version 20

## Installation du projet

### 1. Clonez le dépôt

Clonez ce dépôt Git en local avec la commande suivante :

```bash
git clone git@github.com:ChrystalMoi/P6-Developpez-une-application-full-stack-complete.git
```

### 2. Installation du front-end

Accédez au répertoire front :

```bash
cd P6-Developpez-une-application-full-stack-complete/front
```

Installez les dépendances en exécutant la commande :

```bash
npm install
```

### 3. Construction et démarrage du front-end

Pour construire le projet Angular, exécutez la commande suivante :

```bash
ng build
```

Les artefacts de build seront stockés dans le répertoire dist/.

Pour démarrer un serveur de développement local, exécutez la commande :

```bash
ng serve
```

Accédez ensuite à l'application à l'adresse http://localhost:4200/. L'application se rechargera automatiquement à chaque modification des fichiers source.

### 4. Installation et configuration de la base de données

1. Ouvrez MySQL en tant qu'administrateur.
2. Créez une nouvelle base de données pour l'application.

### 5. Configuration du back-end

Accédez au répertoire back :

```bash
cd ../back
```

Ouvrez le fichier application.properties situé dans src/main/resources/ et configurez les paramètres appropriés pour votre base de données MySQL.

#### Exemple de configuration MySQL classique

```sql
spring.datasource.url=jdbc:mysql://localhost:3306/nom_de_votre_base
spring.datasource.username=votre_utilisateur
spring.datasource.password=votre_mot_de_passe
```

#### Exemple de configuration MySQL avec variables d'environnement

Vous pouvez également configurer votre base de données en utilisant des variables d'environnement :

```sql
spring.datasource.url=jdbc:mysql://${APP_DB_HOST}:${APP_DB_PORT}/${APP_DB_NAME}
spring.datasource.username=${APP_DB_USER}
spring.datasource.password=${APP_DB_PASS}
```
