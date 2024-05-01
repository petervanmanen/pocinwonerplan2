import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Activiteit from './activiteit';
import ActiviteitDetail from './activiteit-detail';
import ActiviteitUpdate from './activiteit-update';
import ActiviteitDeleteDialog from './activiteit-delete-dialog';

const ActiviteitRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Activiteit />} />
    <Route path="new" element={<ActiviteitUpdate />} />
    <Route path=":id">
      <Route index element={<ActiviteitDetail />} />
      <Route path="edit" element={<ActiviteitUpdate />} />
      <Route path="delete" element={<ActiviteitDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActiviteitRoutes;
