import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aanbod.reducer';

export const AanbodDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const aanbodEntity = useAppSelector(state => state.aanbod.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aanbodDetailsHeading">Aanbod</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{aanbodEntity.id}</dd>
          <dt>
            <span id="naam">Naam</span>
          </dt>
          <dd>{aanbodEntity.naam}</dd>
          <dt>
            <span id="subdoelen">Subdoelen</span>
          </dt>
          <dd>{aanbodEntity.subdoelen}</dd>
          <dt>Subdoel</dt>
          <dd>
            {aanbodEntity.subdoels
              ? aanbodEntity.subdoels.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {aanbodEntity.subdoels && i === aanbodEntity.subdoels.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Activiteit</dt>
          <dd>
            {aanbodEntity.activiteits
              ? aanbodEntity.activiteits.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {aanbodEntity.activiteits && i === aanbodEntity.activiteits.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/aanbod" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aanbod/${aanbodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AanbodDetail;
