# logiciel_coiffure

Logiciel destiné au salon de coiffure Éclat Ô Naturel, permettant d'enregistrer les clients, les factures, de gérer l'inventaire automatiquement et avoir un retour sur le chiffre d'affaire.

## Dashboard

Sur le Dashboard, on peut retrouver la liste des clients sur la gauche et la partie sur les factures et chiffre d'affaire sur la droite. Nous pouvons voir le details d'une facture en double cliquant dessus et voir la fiche d'un client également en double cliquant dessus.

<div align="center">
<img src="https://github.com/ValentinLe/logiciel_coiffure/blob/main/screenshots/dashboard.png" alt="demo" width="716" height="402">
</div>

## Fiche client

Sur la fiche client, nous retrouvons différentes informations tel que son nom et prénom. La gestion de la carte de fidélité est intégré au logiciel et s'incrémente automatiquement lors de l'ajout de la facture au client. Nous avons intégré une zone permettant de mettre des notes sur ce qui a été fait lors du passage au salon (au centre). Enfin une zone est prévue pour enregistré les produits qui ont étés achetés par le client (partie de droite).

<div align="center">
<img src="https://github.com/ValentinLe/logiciel_coiffure/blob/main/screenshots/fiche_client.png" alt="demo" width="625" height="417">
</div>

## Facturation d'un client

Lors de la création d'une facture, nous avons la possibilité de rentrer manuellement le prix ou alors d'utiliser le menu de ventes en ajoutant les prestations ou produit que le client à souhaité (menu créer au préalable dans la partie **Gestion du menu des ventes**).

<div align="center">
<img src="https://github.com/ValentinLe/logiciel_coiffure/blob/main/screenshots/facturation.png" alt="demo" width="677" height="511">
</div>

## Gestion du menu des ventes

La partie de gestion des ventes permet de créer son menu de ventes que l'on retrouvera lors de la facturation. Nous pouvons créer soit des produits, soit des prestations en lui donnant un nom et un prix.

<div align="center">
<img src="https://github.com/ValentinLe/logiciel_coiffure/blob/main/screenshots/ventes.png" alt="demo" width="600" height="480">
</div>

## Gestion des stocks

Quand une facture est ajouté, les produits vendus seront décrémenté des stocks ce qui permet de voir l'inventaire directement sur le logiciel. Si il y a une livraison ou si il y a perte de produits (produits périmés ou autres raisons), nous pouvons en ajouter ou en retirer le nombre souhaité.

<div align="center">
<img src="https://github.com/ValentinLe/logiciel_coiffure/blob/main/screenshots/stocks.png" alt="demo" width="524" height="397">
</div>

## Sauvegardes des données

Nayant pas moyen de mettre en place une base de données, ni de serveur, les données sont sauvegardés localement. Donc afin d'avoir une réplication des données nous avons mis en place un système de sauvegarde des clients, des factures et du menu de ventes. Cette sauvegarde peut être effectuée sur plusieurs endroits de l'ordinateur (comme par exemple dans Documents/ ou dans un dossier sur une clé USB).

<div align="center">
<img src="https://github.com/ValentinLe/logiciel_coiffure/blob/main/screenshots/sauvegarde.png" alt="demo" width="602" height="398">
</div>

## Compiler et lancer le programme

Le projet est un projet <a href="https://maven.apache.org/">Maven<a/>, il y a deux scripts dans le dossier scripts/ un pour compiler et un pour lancer le programme.