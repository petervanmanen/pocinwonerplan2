import { IActiviteit } from 'app/shared/model/activiteit.model';
import { IAandachtspunt } from 'app/shared/model/aandachtspunt.model';
import { IOntwikkelwens } from 'app/shared/model/ontwikkelwens.model';

export interface IAanbod {
  id?: number;
  naam?: string | null;
  activiteits?: IActiviteit[] | null;
  aandachtspunts?: IAandachtspunt[] | null;
  ontwikkelwens?: IOntwikkelwens[] | null;
}

export const defaultValue: Readonly<IAanbod> = {};
