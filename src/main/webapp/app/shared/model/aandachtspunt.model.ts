import { IAanbod } from 'app/shared/model/aanbod.model';

export interface IAandachtspunt {
  id?: number;
  code?: number | null;
  naam?: string | null;
  omschrijving?: string | null;
  actief?: boolean | null;
  aanbods?: IAanbod[] | null;
}

export const defaultValue: Readonly<IAandachtspunt> = {
  actief: false,
};
