export interface Article {
  id: number;
  titre: string;
  contenu: string;
  auteurId: number;
  auteurNom: string;
  themeId: number;
  themeNom: string;
  dateCreation: Date;
}
