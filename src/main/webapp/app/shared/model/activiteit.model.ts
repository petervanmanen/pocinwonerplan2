import { IAanbod } from 'app/shared/model/aanbod.model';

export interface IActiviteit {
  id?: number;
  code?: number | null;
  naam?: string | null;
  actiehouder?: string | null;
  afhandeltermijn?: number | null;
  actief?: boolean | null;
  aanbods?: IAanbod[] | null;
}

export const defaultValue: Readonly<IActiviteit> = {
  actief: false,
};
