import dayjs from 'dayjs';
import { ISubdoel } from 'app/shared/model/subdoel.model';

export interface IHoofddoel {
  id?: number;
  begindatum?: dayjs.Dayjs | null;
  naam?: string | null;
  subdoel?: ISubdoel | null;
}

export const defaultValue: Readonly<IHoofddoel> = {};
