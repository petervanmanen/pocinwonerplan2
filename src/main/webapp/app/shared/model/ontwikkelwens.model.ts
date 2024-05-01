export interface IOntwikkelwens {
  id?: number;
  code?: number | null;
  naam?: string | null;
  omschrijving?: string | null;
  actief?: boolean | null;
}

export const defaultValue: Readonly<IOntwikkelwens> = {
  actief: false,
};
