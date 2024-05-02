import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InwonerplanActiviteit from './inwonerplan-activiteit';
import InwonerplanActiviteitDetail from './inwonerplan-activiteit-detail';
import InwonerplanActiviteitUpdate from './inwonerplan-activiteit-update';
import InwonerplanActiviteitDeleteDialog from './inwonerplan-activiteit-delete-dialog';

const InwonerplanActiviteitRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InwonerplanActiviteit />} />
    <Route path="new" element={<InwonerplanActiviteitUpdate />} />
    <Route path=":id">
      <Route index element={<InwonerplanActiviteitDetail />} />
      <Route path="edit" element={<InwonerplanActiviteitUpdate />} />
      <Route path="delete" element={<InwonerplanActiviteitDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InwonerplanActiviteitRoutes;
