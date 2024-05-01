import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ontwikkelwens from './ontwikkelwens';
import OntwikkelwensDetail from './ontwikkelwens-detail';
import OntwikkelwensUpdate from './ontwikkelwens-update';
import OntwikkelwensDeleteDialog from './ontwikkelwens-delete-dialog';

const OntwikkelwensRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ontwikkelwens />} />
    <Route path="new" element={<OntwikkelwensUpdate />} />
    <Route path=":id">
      <Route index element={<OntwikkelwensDetail />} />
      <Route path="edit" element={<OntwikkelwensUpdate />} />
      <Route path="delete" element={<OntwikkelwensDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OntwikkelwensRoutes;
