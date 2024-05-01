import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Aanbod from './aanbod';
import AanbodDetail from './aanbod-detail';
import AanbodUpdate from './aanbod-update';
import AanbodDeleteDialog from './aanbod-delete-dialog';

const AanbodRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Aanbod />} />
    <Route path="new" element={<AanbodUpdate />} />
    <Route path=":id">
      <Route index element={<AanbodDetail />} />
      <Route path="edit" element={<AanbodUpdate />} />
      <Route path="delete" element={<AanbodDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AanbodRoutes;
