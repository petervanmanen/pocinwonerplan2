import { IHoofddoel } from 'app/shared/model/hoofddoel.model';
import { IAanbod } from 'app/shared/model/aanbod.model';

export interface IInwonerplan {
  id?: number;
  bsn?: string;
  hoofddoel?: IHoofddoel | null;
  aanbod?: IAanbod | null;
}

export const defaultValue: Readonly<IInwonerplan> = {};
