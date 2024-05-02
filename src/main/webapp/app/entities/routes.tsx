import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subdoel from './subdoel';
import Aanbod from './aanbod';
import Aandachtspunt from './aandachtspunt';
import Activiteit from './activiteit';
import Ontwikkelwens from './ontwikkelwens';
import Inwonerplan from './inwonerplan';
import Inwonerprofiel from './inwonerprofiel';
import InwonerplanSubDoel from './inwonerplan-sub-doel';
import InwonerplanActiviteit from './inwonerplan-activiteit';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}

        <Route path="subdoel/*" element={<Subdoel />} />
        <Route path="aanbod/*" element={<Aanbod />} />
        <Route path="aandachtspunt/*" element={<Aandachtspunt />} />
        <Route path="activiteit/*" element={<Activiteit />} />
        <Route path="ontwikkelwens/*" element={<Ontwikkelwens />} />
        <Route path="inwonerplan/*" element={<Inwonerplan />} />
        <Route path="inwonerprofiel/*" element={<Inwonerprofiel />} />
        <Route path="inwonerplan-sub-doel/*" element={<InwonerplanSubDoel />} />
        <Route path="inwonerplan-activiteit/*" element={<InwonerplanActiviteit />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
