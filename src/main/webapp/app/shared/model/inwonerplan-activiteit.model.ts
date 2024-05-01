import { IAanbod } from 'app/shared/model/aanbod.model';

export interface IInwonerplanActiviteit {
  id?: string;
  naam?: string | null;
  actief?: boolean | null;
  afhandeltermijn?: number | null;
  aanbod?: IAanbod | null;
}

export const defaultValue: Readonly<IInwonerplanActiviteit> = {
  actief: false,
};
