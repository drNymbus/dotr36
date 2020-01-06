## Dukes Of The realm
Groupe 36

# LANCEMENT
Aucun argument n'est nécessaire pour le lancement.
JavaFX doit cependant être installé.

# SYNOPIS
Le jeu se déroule dans un royaume lointain, à la stabilité discutable, dans lequel le trône a
été laissé vacant suite à diverses intrigues qu’il ne nous appartient pas de décrire ici.
Chaque joueur incarne un duc, en lutte pour devenir le nouveau roi. Pour cela, ils doivent
vassaliser les autres en conquérant leur terres.

# REGLE:
Le but est donc de conquérir les châteaux adverses à l'aide de vos unités.
Il y a 5 châteaux dans le royaume, vous en contrôlez un, votre ennemie en contrôle
un autre, et 3 châteaux appartiennent à des ducs sans ambitions.

La partie se fini si vous ou votre ennemi ne possédez plus de châteaux.

# COMMANDE
La touche P permet de mettre le jeu en pause.
La touche R permet de reprendre la jeu.
Un clic gauche sur un château le sélectionnera,
et ses informations seront affichés(Appartenance, cible, niveau, trésor ...) dans la partie gauche de votre écran.
Un clic droit sur un château allié après l'avoir selectionner vous ouvrira un menu deroulant,
vous aurez alors accès aux options suivantes:
    Produire une unité,
    Arrêter la production du dernier element (et ainsi récupérer l'argent investit)
    Monter le chateau de niveau,
    Arreter d'envoyer des unitees.

En cliquant sur un chateau qui ne vous appartient pas, vous avez alors l'option de l'attaquer
ainsi que de choisir combien d'unitees a envoyer (entre 1 et 5, les unitees sont envoyes dans l'ordre de creation).

Differentes unitees peuvent etres crees:
* Piquier (100 Gold): unitee basique
    * 1 Life Point,
    * 1 Attack,
    * 2 Speed
* Chevalier (500 Gold): unitee plus rapide et plus forte
    * 3 Life Point,
    * 6 Attack,
    * 5 Speed
* Onagre (1000 Gold): unitee extrement puissante et tenace, cependant cette unitee peu rapide
    * 5 Life Point,
    * 10 Attack,
    * 1 Speed

# A AMELIORER
    Pathfinding peut être améliorer pour contourner le chateau cible jusqu'a la porte.
    Les touches permmettant de Pause/Resume n'ont parfois aucun effet.
    Certains chateau peuvent apparaitre dans la bar d'UI ou au bord de l'ecran.
