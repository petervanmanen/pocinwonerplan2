import { ISubdoel } from 'app/shared/model/subdoel.model';
import { IActiviteit } from 'app/shared/model/activiteit.model';

export interface IAanbod {
  id?: number;
  naam?: string | null;
  subdoelen?: string | null;
  subdoels?: ISubdoel[] | null;
  activiteits?: IActiviteit[] | null;
}

export const defaultValue: Readonly<IAanbod> = {};
