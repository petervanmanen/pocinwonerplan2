import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Inwonerprofiel from './inwonerprofiel';
import InwonerprofielDetail from './inwonerprofiel-detail';
import InwonerprofielUpdate from './inwonerprofiel-update';
import InwonerprofielDeleteDialog from './inwonerprofiel-delete-dialog';

const InwonerprofielRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Inwonerprofiel />} />
    <Route path="new" element={<InwonerprofielUpdate />} />
    <Route path=":id">
      <Route index element={<InwonerprofielDetail />} />
      <Route path="edit" element={<InwonerprofielUpdate />} />
      <Route path="delete" element={<InwonerprofielDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InwonerprofielRoutes;
