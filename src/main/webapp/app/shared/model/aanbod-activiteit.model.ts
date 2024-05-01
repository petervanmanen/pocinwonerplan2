import { IActiehouder } from 'app/shared/model/actiehouder.model';
import { IAanbod } from 'app/shared/model/aanbod.model';

export interface IAanbodActiviteit {
  id?: string;
  naam?: string | null;
  actief?: boolean | null;
  afhandeltermijn?: number | null;
  actiehouder?: IActiehouder | null;
  aanbods?: IAanbod[] | null;
}

export const defaultValue: Readonly<IAanbodActiviteit> = {
  actief: false,
};
