import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './activiteit.reducer';

export const ActiviteitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const activiteitEntity = useAppSelector(state => state.activiteit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="activiteitDetailsHeading">Activiteit</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{activiteitEntity.id}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{activiteitEntity.code}</dd>
          <dt>
            <span id="naam">Naam</span>
          </dt>
          <dd>{activiteitEntity.naam}</dd>
          <dt>
            <span id="actiehouder">Actiehouder</span>
          </dt>
          <dd>{activiteitEntity.actiehouder}</dd>
          <dt>
            <span id="afhandeltermijn">Afhandeltermijn</span>
          </dt>
          <dd>{activiteitEntity.afhandeltermijn}</dd>
          <dt>
            <span id="actief">Actief</span>
          </dt>
          <dd>{activiteitEntity.actief ? 'true' : 'false'}</dd>
          <dt>Aanbod</dt>
          <dd>
            {activiteitEntity.aanbods
              ? activiteitEntity.aanbods.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {activiteitEntity.aanbods && i === activiteitEntity.aanbods.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/activiteit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/activiteit/${activiteitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ActiviteitDetail;
