export interface IAandachtspunt {
  id?: number;
  code?: number | null;
  naam?: string | null;
  omschrijving?: string | null;
  actief?: boolean | null;
}

export const defaultValue: Readonly<IAandachtspunt> = {
  actief: false,
};
