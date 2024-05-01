import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}

      <MenuItem icon="asterisk" to="/subdoel">
        Subdoel
      </MenuItem>
      <MenuItem icon="asterisk" to="/aanbod">
        Aanbod
      </MenuItem>
      <MenuItem icon="asterisk" to="/aandachtspunt">
        Aandachtspunt
      </MenuItem>
      <MenuItem icon="asterisk" to="/activiteit">
        Activiteit
      </MenuItem>
      <MenuItem icon="asterisk" to="/ontwikkelwens">
        Ontwikkelwens
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
