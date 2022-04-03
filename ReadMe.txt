/*************************************ACTION************************************\
	Déplacement : bouton 'z' = avancer					
		      bouton 's' = reculer					
		      bouton 'q' = gauche					
		      bouton 'd' = droite					
		      bouton 'c' = monter					
		      bouton 'v' = descendre
			bouton 'l' = allumer/eteindre la lumière
			bouton 'f' = allumer/eteindre la lampe torche (dans le cas d'utilisation de MyLightingShaders.java)	
										
 	Orientation : click gauche puis déplacement de la souris dans la 	
		      direction souhaité.					
										
	Reste de la vue : bouton 'r' reset la positon au centre de la 		
			  première room.					
										
	Changer de model : bouton 'a' permet de changer de model au 		
  			   centre de la deuxième room				
\*******************************************************************************/


J'ai créé les shaders de base Gouraud(GouraudShaders.java), Lambert(LambertShaders.java) et Phong(PhongShaders.java), ces trois shaders appliquent 
Une lumière globale et illumine les faces en fonction de leurs normales.

J'ai également créé deux autres shaders le premier (SpotLightShaders.java) qui éclaire seulement zone dans une direction donnée.
Le second (MyLightingShaders.java) est une reprise du shaders Phong avec quelques ajouts d'abord, la lumière est atténuée en fonction
de la distance puis l'ajout d'une lampe torche au joueur.

Tous les shaders ici présents(sauf le shaders Gouraud) peuvent posséder plusieur sources lumineuses mais pour se faire il faut modifier le nombre de sources avec : 
"#define NR_POINT_LIGHTS" qui est stocké dans les _frag.glsl.
Et puis il faudra ajouter (ou retirer) les informations de la/des lumières dans la fonction creationLights(LightingShaders shaders) qui est dans la classe Scene.java.
(S'il y a plus de définition de light que le nombre de light (#define NR_POINT_LIGHTS), ceci ne pose pas de problème alors que le cas inverse oui)