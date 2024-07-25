import { Theme } from '../features/articles/interfaces/theme.interface';
export interface User {
  id: number;
  nom: string;
  email: string;
  roles: string;
  subscriptions: Theme[];
}
