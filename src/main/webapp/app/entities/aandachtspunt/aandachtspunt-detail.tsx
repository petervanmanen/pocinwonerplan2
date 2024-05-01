import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aandachtspunt.reducer';

export const AandachtspuntDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const aandachtspuntEntity = useAppSelector(state => state.aandachtspunt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aandachtspuntDetailsHeading">Aandachtspunt</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{aandachtspuntEntity.id}</dd>
          <dt>
            <span id="code">Code</span>
          </dt>
          <dd>{aandachtspuntEntity.code}</dd>
          <dt>
            <span id="naam">Naam</span>
          </dt>
          <dd>{aandachtspuntEntity.naam}</dd>
          <dt>
            <span id="omschrijving">Omschrijving</span>
          </dt>
          <dd>{aandachtspuntEntity.omschrijving}</dd>
          <dt>
            <span id="actief">Actief</span>
          </dt>
          <dd>{aandachtspuntEntity.actief ? 'true' : 'false'}</dd>
          <dt>Aanbod</dt>
          <dd>
            {aandachtspuntEntity.aanbods
              ? aandachtspuntEntity.aanbods.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {aandachtspuntEntity.aanbods && i === aandachtspuntEntity.aanbods.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/aandachtspunt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aandachtspunt/${aandachtspuntEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AandachtspuntDetail;
