export interface Article {
  id: number;
  titre: string;
  contenu: string;
  utilisateur_id: number;
  nomUtilisateur: string;
  theme_id: number;
  theme: string;
  dateCreation: Date;
}
