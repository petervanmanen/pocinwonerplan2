import { IAandachtspunt } from 'app/shared/model/aandachtspunt.model';
import { IOntwikkelwens } from 'app/shared/model/ontwikkelwens.model';
import { IAanbod } from 'app/shared/model/aanbod.model';

export interface ISubdoel {
  id?: number;
  code?: number | null;
  naam?: string | null;
  actief?: boolean | null;
  aandachtspunt?: IAandachtspunt | null;
  ontwikkelwens?: IOntwikkelwens | null;
  aanbods?: IAanbod[] | null;
}

export const defaultValue: Readonly<ISubdoel> = {
  actief: false,
};
