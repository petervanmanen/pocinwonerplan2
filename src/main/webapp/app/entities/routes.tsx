import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subdoel from './subdoel';
import Aanbod from './aanbod';
import Aandachtspunt from './aandachtspunt';
import Activiteit from './activiteit';
import Ontwikkelwens from './ontwikkelwens';
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
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
